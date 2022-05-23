package startup

import vtk.vtkNativeLibrary

object VtkLoader {
    operator fun invoke() {
        //try loading libraries
        if (!vtkNativeLibrary.LoadAllNativeLibraries()) {
            vtkNativeLibrary.values()
                .filter { !it.IsLoaded() }
                .forEach {lib->
                    if (!lib.IsLoaded()) {
                        println(lib.GetLibraryName() + " not loaded")
                    }
                }
            throw IllegalStateException("some libraries not loaded")
        }

//        vtkNativeLibrary.DisableOutputWindow(null)
    }
}