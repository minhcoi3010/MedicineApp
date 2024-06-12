//package com.example.btlandroidapi;
//
//import android.app.Application;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.widget.Toast;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.btlandroidapi.model.Medicine;
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.btlandroidapi.model.Medicine;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CartViewModel extends ViewModel {
//    private static CartViewModel instance;
//    private MutableLiveData<List<Medicine>> cartList;
//
//    public static synchronized CartViewModel getInstance() {
//        if (instance == null) {
//            instance = new CartViewModel();
//        }
//        return instance;
//    }
//
//    private CartViewModel() {
//        cartList = new MutableLiveData<>();
//        cartList.setValue(new ArrayList<>());
//    }
//
//    public LiveData<List<Medicine>> getCartList() {
//        return cartList;
//    }
//
//    public void addToCart(Medicine medicine) {
//        List<Medicine> currentList = cartList.getValue();
//        if (currentList != null) {
//                currentList.add(medicine);
//                cartList.setValue(currentList);
//        }
//    }
//
//    public void removeFromCart(int position) {
//        List<Medicine> currentList = cartList.getValue();
//        if (currentList != null && position >= 0 && position < currentList.size()) {
//            currentList.remove(position);
//            cartList.setValue(currentList);
//        }
//    }
//
//    public void clearCart() {
//        cartList.setValue(new ArrayList<>());
//    }
//}
//
//
//
//

package com.example.btlandroidapi.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.btlandroidapi.singleton.CustomerSingleton;
import com.example.btlandroidapi.model.Medicine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {
    private static CartViewModel instance;
    private MutableLiveData<List<Medicine>> cartList;
    private static final String SHARED_PREFS = "CART_PREFS";
    private static final String CART_KEY_PREFIX = "CART_";
    private final SharedPreferences preferences;

    public static synchronized CartViewModel getInstance(Context context) {
        if (instance == null) {
            instance = new CartViewModel(context.getApplicationContext());
        }
        return instance;
    }

    private CartViewModel(Context context) {
        preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        cartList = new MutableLiveData<>();
        loadCart();
    }

    public LiveData<List<Medicine>> getCartList() {
        loadCart();
        return cartList;
    }

//    public void addToCart(Medicine medicine) {
//        loadCart();
//        List<Medicine> currentList = cartList.getValue();
//        if (currentList != null) {
//            currentList.add(medicine);
//            cartList.setValue(currentList);
//            saveCart();
//        }
//    }
    public void addToCart(Medicine medicine) {
        loadCart();
        List<Medicine> currentList = cartList.getValue();
        if (currentList != null) {
            boolean alreadyExists = false;
            for (Medicine item : currentList) {
                if (item.getMaThuoc().equals(medicine.getMaThuoc())) { // Sử dụng một trường như ID để so sánh
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                currentList.add(medicine);
                cartList.setValue(currentList);
                saveCart();
            } else {

            }
        }
    }


    public void removeFromCart(int position) {
        List<Medicine> currentList = cartList.getValue();
        if (currentList != null && position >= 0 && position < currentList.size()) {
            currentList.remove(position);
            cartList.setValue(currentList);
            saveCart();
        }
    }

    public void clearCart() {
        cartList.setValue(new ArrayList<>());
        saveCart();
    }

    private void saveCart() {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String jsonCart = gson.toJson(cartList.getValue());
        String cartKey = CART_KEY_PREFIX + CustomerSingleton.getInstance().getCustomerId();
        editor.putString(cartKey, jsonCart);
        editor.apply(); // Use apply instead of commit for background operations
    }

    private void loadCart() {
        Gson gson = new Gson();
        String cartKey = CART_KEY_PREFIX + CustomerSingleton.getInstance().getCustomerId();
        String jsonCart = preferences.getString(cartKey, null);
        if (jsonCart != null) {
            List<Medicine> loadedCart = gson.fromJson(jsonCart, new TypeToken<List<Medicine>>() {}.getType());
            cartList.setValue(loadedCart);
        } else {
            cartList.setValue(new ArrayList<>());
        }
    }
}
