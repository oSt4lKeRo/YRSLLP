package JavaClass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ModuleToProfileSharedViewModel extends ViewModel {

    private MutableLiveData<List<StructureClass.Module>> modules = new MutableLiveData<>();

    public LiveData<List<StructureClass.Module>> getModules() {
        return modules;
    }

    public void setModules(List<StructureClass.Module> newModules) {
        modules.setValue(newModules);
    }

}
