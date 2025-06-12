import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.allfreeapps.theballgame.MyApplication
import com.allfreeapps.theballgame.viewModels.BallGameViewModel

class BallGameViewModelFactory(private val application: MyApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BallGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BallGameViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}