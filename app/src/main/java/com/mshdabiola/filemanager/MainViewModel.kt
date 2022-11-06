package com.mshdabiola.filemanager

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.filemanager.data.FileRepository
import com.mshdabiola.filemanager.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.*


@HiltViewModel
class MainViewModel
@Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val fileRepository: FileRepository,
    private val fileActions: FileActions
) : ViewModel() {


    private val _fileManagerUiState = MutableStateFlow(FileManagerUiState(""))
    val fileManagerUiState = _fileManagerUiState.asStateFlow()


    private val initMainUiState = MainUiState(
        storageUiStates = emptyList(),
        recentFiles = emptyList(),
        categoryUiStates = emptyList(),
        getAllRecentFileUistate = this::getRecentFiles
    )
    private val _mainUiState = MutableStateFlow(initMainUiState)
    val mainUiState = _mainUiState.asStateFlow()

    val storagePro = arrayOf(
        "Internal Storage " to R.drawable.ic_baseline_phone_android_24,
        "External SD Card" to R.drawable.ic_baseline_sd_storage_24
    )

    private val _selectUiStat = MutableStateFlow(SelectUiState())
    val selectUiState = _selectUiStat.asStateFlow()

    init {

        //init file manager state
        viewModelScope.launch {

            val initActionBottomSheetUiState = ActionBottomSheetUiState(
                show = false,
                onSelect = {
                    selectCurrentFile(true)
                },
                onCopy = {
                    _selectUiStat.value = selectUiState.value.copy(state = CopyMoveEnum.COPY)
                    showBottomSheetForMoveAndCopy("Copy File To")
                },
                onMove = {
                    _selectUiStat.value = selectUiState.value.copy(state = CopyMoveEnum.MOVE)
                    showBottomSheetForMoveAndCopy("Move File To")
                },
                onShare = {
                    sharePath()
                },
                onRename = { onBottomRename() },

                onDelete = { showDeleteDialog() },
                onOpenWith = { openFileWith() },

                onDismiss = { hideBottomSheet() }

            )
            val initMoveAndCopyBottomSheetUiState = MoveAndCopyBottomSheetUiState(
                show = false,
                actionName = "Move to",
                listOfStorageUiState = emptyList(),

                onDismiss = { hideBottomSheetMoveAndCopy() }
            )

            val initRenameUiState = RenameUiState(
                show = false,
                currentName = "",
                onDismissRequest = this@MainViewModel::onDissRename,
                onRename = this@MainViewModel::onRename,
                onNameChange = this@MainViewModel::onNameChange,
                errorOccur = false,
                errorMsg = "",
            )

            val initDeleteUiState = DeleteUiState(
                show = false,
                onCancel = { hideDeleteDialog() },
                onDelete = { deleteFile() }
            )



            _fileManagerUiState.value = fileManagerUiState.value.copy(
                name = "File Manager",
                fileUiStateList = emptyList(),
                actionBottomSheetUiState = initActionBottomSheetUiState,
                moveAndCopyBottomSheetUiState = initMoveAndCopyBottomSheetUiState,
                renameUiState = initRenameUiState,
                deleteUiState = initDeleteUiState,
                getAllFiles = this@MainViewModel::getAllFileUiState,
                isInSelectedMode = false,
                openActionBottomSheet = { showBottomSheet() },
                onDeselectedAllFile = this@MainViewModel::onDeselectAllFile,
                onSelectedAllFile = { onSelectAllFile() }
            )

        }

        //init selectUistate
        viewModelScope.launch {
            _selectUiStat.value = selectUiState.value.copy(
                folderName = "Internal Storage",
                listOfFileUiState = emptyList(),
                getAllDirectory = this@MainViewModel::getAllDirectory,
                onSelectClick = this@MainViewModel::selectClick,
                addNewFolder = {}
            )
        }


        //get storage and categories
        viewModelScope.launch(Dispatchers.IO) {
            val storage = getAllStorageUiState()
            _mainUiState.value = _mainUiState.value.copy(
                storageUiStates = storage,
                categoryUiStates = fileRepository.listOfCategory,
            )

            _fileManagerUiState.value = fileManagerUiState.value.copy(
                moveAndCopyBottomSheetUiState = fileManagerUiState.value.moveAndCopyBottomSheetUiState.copy(
                    listOfStorageUiState = storage
                )
            )
        }

        //get recent file
        viewModelScope.launch {
            getRecentFiles()
        }

        //update recent file when any file change
        viewModelScope.launch {
            fileManagerUiState
                .map { it.fileUiStateList }
                .distinctUntilChanged { old, new -> old.containsAll(new) }
                .collect {
                    getRecentFiles()
                }
        }


    }

    // get State
    private fun getAllStorageUiState(): List<StorageUiState> {
        return fileRepository
            .getStorage()
            .mapIndexed { index, path ->
                val pair = storagePro[index]
                StorageUiState(name = pair.first, icon = pair.second, path = path)
            }

    }

    private fun getNewFileUiState(path: Path): FileUiState {
        return FileUiState(
            path = path,
            isSelected = false,
            onSelectedClick = {
                onSelectClicked(it)
            },
            onMoreClicked = { onMoreOptionClicked(it) },
            onClicked = { onFileClick(it) }
        )
    }

    private fun getAllFileUiState(pathStr: String) {


        viewModelScope.launch(Dispatchers.IO) {
            val fileUiStates = fileRepository
                .getAllFileInDirectory(pathStr)
                .map { path ->
                    getNewFileUiState(path)
                }

            _fileManagerUiState.value = _fileManagerUiState.value.copy(
                fileUiStateList = fileUiStates
            )
        }
    }

    private fun getRecentFiles() {
        viewModelScope.launch(Dispatchers.IO) {

            val recentFile = fileRepository.getRecentFiles()
                .map {
                    FileUiState(path = it, onClicked = { id ->
                        val path = mainUiState.value.recentFiles.find { it.id == id }!!.path
                        fileActions.openFile(path.toFile())
                    })
                }
                .toList()

            _mainUiState.value = _mainUiState.value.copy(recentFiles = recentFile)
        }
    }

    private fun setCurrentFileUiState(id: Long) {
        viewModelScope.launch {
            val currentFileUiState =
                fileManagerUiState.value.fileUiStateList.first { id == it.id }

            _fileManagerUiState.value =
                fileManagerUiState.value.copy(currentFileUiState = currentFileUiState)
        }

    }

    //show and hide bottom sheet section
    private fun showBottomSheet() {
        val actionBottomSheetUiState =
            fileManagerUiState.value.actionBottomSheetUiState.copy(show = true)

        _fileManagerUiState.value = _fileManagerUiState.value.copy(
            actionBottomSheetUiState = actionBottomSheetUiState
        )
    }

    private fun hideBottomSheet() {
        _fileManagerUiState.value = _fileManagerUiState.value.copy(
            actionBottomSheetUiState = fileManagerUiState.value.actionBottomSheetUiState.copy(show = false)
        )
    }

    //show and hide bottom sheet  for move and copy section
    private fun showBottomSheetForMoveAndCopy(actionName: String) {
        val moveAndCopyBottomSheetUiState =
            fileManagerUiState.value.moveAndCopyBottomSheetUiState.copy(
                show = true,
                actionName = actionName
            )

        _fileManagerUiState.value = _fileManagerUiState.value.copy(
            moveAndCopyBottomSheetUiState = moveAndCopyBottomSheetUiState
        )
    }

    private fun hideBottomSheetMoveAndCopy() {
        _fileManagerUiState.value = _fileManagerUiState.value.copy(
            moveAndCopyBottomSheetUiState = fileManagerUiState.value.moveAndCopyBottomSheetUiState.copy(
                show = false
            )
        )
    }

    //file click section
    private fun onMoreOptionClicked(id: Long) {
        setCurrentFileUiState(id)
        showBottomSheet()
    }

    private fun onFileClick(id: Long) {
        setCurrentFileUiState(id)
        val file = fileManagerUiState.value.currentFileUiState.path.toFile()
        fileActions.openFile(file)

    }

    private fun onSelectClicked(id: Long) {

        setCurrentFileUiState(id)
        val currentFileUiState = fileManagerUiState.value.currentFileUiState
        selectCurrentFile(!currentFileUiState.isSelected)

    }

    /**
    folder property

    select
    move to
    copy to
    rename
    delete permanently
    folder info

     */

    //select section
    private fun selectCurrentFile(yes: Boolean) {
        val oldFileUiStateList = fileManagerUiState.value.fileUiStateList
        val currentFile = fileManagerUiState.value.currentFileUiState
        val fileIndex = oldFileUiStateList.indexOfFirst { it.id == currentFile.id }


        val newFileUiStateList = oldFileUiStateList.toMutableList().apply {
            this[fileIndex] = currentFile.copy(isSelected = yes)
        }


        var fileManagerUiState = fileManagerUiState.value.copy(fileUiStateList = newFileUiStateList)


        //change to select mode

        if (yes) {
            fileManagerUiState = fileManagerUiState.copy(isInSelectedMode = true)
        }
        _fileManagerUiState.value = fileManagerUiState
    }

    private fun onDeselectAllFile() {
        val newFileUiStateList = fileManagerUiState.value
            .fileUiStateList.toMutableList()

        newFileUiStateList.forEachIndexed { index, _ ->
            newFileUiStateList[index] = newFileUiStateList[index].copy(isSelected = false)

        }

        _fileManagerUiState.value = fileManagerUiState.value.copy(
            fileUiStateList = newFileUiStateList,
            isInSelectedMode = false
        )

    }

    private fun onSelectAllFile() {
        val newFileUiStateList = fileManagerUiState.value
            .fileUiStateList.toMutableList()

        newFileUiStateList.forEachIndexed { index, _ ->
            newFileUiStateList[index] = newFileUiStateList[index].copy(isSelected = true)

        }

        _fileManagerUiState.value = fileManagerUiState.value.copy(
            fileUiStateList = newFileUiStateList
        )

    }


    /**
    select
    share
    open with
    move to
    copy to
    rename
    add to favourite
    move to trash
    move to save folder
    delete permanently
    file info
     */


    //Select Screen

    private fun getAllDirectory(pathString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val path = Path(pathString)

            val listOfDir = path
                .listDirectoryEntries()
                .filter { it.isDirectory() && it.isHidden().not() }
                .sortedWith(compareBy { it.name })
                .map { FileUiState(path = it) }

            _selectUiStat.value = selectUiState.value.copy(
                listOfFileUiState = listOfDir,
                folderName = path.name,
                currentPath = path
            )
        }
    }

    private fun selectClick(current: String) {
        viewModelScope.launch(Dispatchers.IO) {


            val selectList = fileManagerUiState
                .value
                .fileUiStateList
                .filter { it.isSelected }

            when {
                selectUiState.value.state == CopyMoveEnum.COPY && selectList.isNotEmpty() -> {
                    selectList.forEach {
                        copyOneFile(it.path)
                    }
                }
                selectUiState.value.state == CopyMoveEnum.COPY -> {
                    val fileToCopy = fileManagerUiState.value.currentFileUiState.path

                    fileToCopy.let {
                        copyOneFile(it)
                    }
                }
                selectUiState.value.state == CopyMoveEnum.MOVE && selectList.isNotEmpty() -> {
                    selectList.forEach {
                        moveOneFile(it.path)
                    }
                }
                selectUiState.value.state == CopyMoveEnum.MOVE -> {
                    val fileToMove = fileManagerUiState.value.currentFileUiState.path

                    moveOneFile(fileToMove)
                }
            }
        }

    }

    private fun moveOneFile(fileToMove: Path) {

        val destFolder = selectUiState.value.currentPath!!


        val list = fileManagerUiState.value.fileUiStateList.toMutableList()
        list.removeIf { it.path == fileToMove }

        _fileManagerUiState.value = fileManagerUiState.value.copy(fileUiStateList = list)

        //fileToMove.moveTo(fileToCopyInto, true)

        moveFile(fileToMove, destFolder)


    }

    private fun moveFile(source: Path, target: Path) {


        val neww = target.resolve(source.name)
        source.moveTo(neww)


    }

    private fun copyOneFile(fileToCopy: Path) {
        val destFolder = selectUiState.value.currentPath!!

        val fileToCopyInto = destFolder.resolve(fileToCopy.name)
        //fileToCopy.copyTo(fileToCopyInto, true)
        copyFile(fileToCopy, destFolder)
    }

    private fun copyFile(source: Path, target: Path) {
        if (source.isDirectory()) {
            val targetHome = target.resolve(source.name)

            targetHome.createDirectory()

            source
                .toFile()
                .walkTopDown()
                .toList()

                .map { it.toPath() }

                .forEach {
                    val file = source.relativize(it)
                    val newPath = targetHome.resolve(file)

                    Log.e("old path ", it.pathString)

                    Log.e("new path", newPath.pathString)
                    if (it.isDirectory() && !it.exists()) {
                        it.createDirectories()
                    } else {
                        try {
                            it.copyTo(newPath)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                }
        } else {
            val newPath = target.resolve(source.name)

            source.copyTo(newPath, true)
        }

    }

    //share file

    private fun sharePath() {
        val path = fileManagerUiState.value.currentFileUiState.path
        path.let {
            fileActions.shareFile(listOf(it))
        }

    }

    //rename

    private fun onDissRename() {
        val renameUiState = fileManagerUiState.value.renameUiState.copy(show = false)
        _fileManagerUiState.value = fileManagerUiState.value.copy(renameUiState = renameUiState)
    }

    private fun onNameChange(input: String) {
        val renameUiState = fileManagerUiState.value.renameUiState.copy(currentName = input)
        _fileManagerUiState.value = fileManagerUiState.value.copy(renameUiState = renameUiState)

        if (renameUiState.currentName.contains("")) {
            //error
        }
    }

    private fun onRename() {

        val oldName = fileManagerUiState.value.currentFileUiState.path
        val newName = fileManagerUiState.value.renameUiState.currentName

        val newPath =
            if (oldName.isDirectory().not())
                "${oldName.parent.pathString}/${newName}.${oldName.extension}"
            else
                "${oldName.parent.pathString}/${newName}"

        val index = fileManagerUiState.value.fileUiStateList.indexOfFirst { it.path == oldName }

        val list = fileManagerUiState.value.fileUiStateList.toMutableList()

        var fileUiState = list.removeAt(index)

        // _fileManagerUiState.value = fileManagerUiState.value.copy(fileUiStateList = list)

        val isRename = oldName.toFile().renameTo(File(newPath))

        if (isRename) {
            fileUiState = fileUiState.copy(path = Path(newPath))

            list.add(index, fileUiState)

            val renameUiState = fileManagerUiState.value.renameUiState.copy(currentName = "")

            _fileManagerUiState.value =
                fileManagerUiState.value.copy(fileUiStateList = list, renameUiState = renameUiState)

        }
    }

    private fun onBottomRename() {
        val renameUiState = fileManagerUiState.value.renameUiState.copy(
            show = true,
            extension = fileManagerUiState.value.currentFileUiState.path.extension
        )

        _fileManagerUiState.value = fileManagerUiState.value.copy(renameUiState = renameUiState)
    }

    //delete

    private fun deleteFile() {
        val oldName = fileManagerUiState.value.currentFileUiState.path

        val list = fileManagerUiState.value.fileUiStateList.toMutableList()

        list.removeIf { it.path == oldName }

        if (oldName.isDirectory()) {
            oldName.toFile().deleteRecursively()
        } else {
            oldName.deleteIfExists()
        }


        val deleteUiState = fileManagerUiState.value.deleteUiState.copy(show = false)

        _fileManagerUiState.value =
            fileManagerUiState.value.copy(fileUiStateList = list, deleteUiState = deleteUiState)

    }

    private fun hideDeleteDialog() {
        _fileManagerUiState.value = fileManagerUiState.value.copy(
            deleteUiState = fileManagerUiState.value.deleteUiState.copy(show = false)
        )
    }

    private fun showDeleteDialog() {
        _fileManagerUiState.value = fileManagerUiState.value.copy(
            deleteUiState = fileManagerUiState.value.deleteUiState.copy(show = true)
        )
    }


//    file info

    //open file with

    private fun openFileWith() {
        val path = fileManagerUiState.value.currentFileUiState.path

        fileActions.openFile(path.toFile())
    }


}