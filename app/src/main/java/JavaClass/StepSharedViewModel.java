package JavaClass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import JavaClass.StructureClass;

public class StepSharedViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<StructureClass.Step>> steps = new MutableLiveData<>();
    private final MutableLiveData<Integer> actualPage = new MutableLiveData<>();

    public void setSteps(ArrayList<StructureClass.Step> stepList) {
        steps.setValue(stepList);
    }

    public LiveData<ArrayList<StructureClass.Step>> getSteps() {
        return steps;
    }

    public void setActualPage(int page) {
        actualPage.setValue(page);
    }

    public LiveData<Integer> getActualPage() {
        return actualPage;
    }
}
