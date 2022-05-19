package startup

import vtk.vtkNativeLibrary

object VtkLoader {
    operator fun invoke() {
        println(System.getProperty("java.library.path"))
        println(System.getProperty("LD_LIBRARY_PATH"))

        //try loading libraries
        vtkNativeLibrary.values().forEach { lib ->
            vtkNativeLibrary.LoadNativeLibraries(lib)
        }


        vtkNativeLibrary.DisableOutputWindow(null)
    }
}