package JavaClass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class DictionarySharedViewModel extends ViewModel {

    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Category> selectedCategory = new MutableLiveData<>();

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categoryList) {
        categories.setValue(categoryList);
    }

    public LiveData<Category> getSelectedCategory() {
        return selectedCategory;
    }

    public void selectCategory(Category category) {
        selectedCategory.setValue(category);
    }
}
