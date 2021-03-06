package com.frezzcoding.bolosassuncao.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frezzcoding.bolosassuncao.data.OperationCallBack
import com.frezzcoding.bolosassuncao.data.UploadCallBack
import com.frezzcoding.bolosassuncao.models.Product
import com.frezzcoding.bolosassuncao.models.ProductDataSource


class ProductViewModel(private val repository : ProductDataSource) : ViewModel() {

    //private val _products = MutableLiveData<List<Product>>().apply { value = emptyList() }
    private val _products = MutableLiveData<ArrayList<Product>>()
    val products : LiveData<ArrayList<Product>> = _products

    private val _isViewLoading=MutableLiveData<Boolean>()
    val isViewLoading:LiveData<Boolean> = _isViewLoading

    private val _onMessageError=MutableLiveData<Any>()
    val onMessageError:LiveData<Any> = _onMessageError

    private val _isEmptyList=MutableLiveData<Boolean>()
    val isEmptyList:LiveData<Boolean> = _isEmptyList

    private val _upload = MutableLiveData<Boolean>()
    val upload : LiveData<Boolean> = _upload

    private val _update = MutableLiveData<Boolean>()
    val update : LiveData<Boolean> = _update

    private val _delete = MutableLiveData<Boolean>()
    val delete : LiveData<Boolean> = _delete

    private val OPERATION_DELETE = 0;
    private val OPERATION_UPDATE = 1;
    private val OPERATION_UPLOAD = 2;

    fun delete(product : Product){
        _isViewLoading.postValue(true)
        repository.geneticOperation(OPERATION_DELETE, product, object:UploadCallBack<Boolean>{
            override fun onSuccess(data: Boolean) {
                _isViewLoading.postValue(false)
                _delete.value = data
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _upload.value = false
                _onMessageError.postValue(error)
            }

        })
    }

    fun upload(product : Product){
        _isViewLoading.postValue(true)
        repository.geneticOperation(OPERATION_UPLOAD, product, object:UploadCallBack<Boolean>{
            override fun onSuccess(data: Boolean) {
                _isViewLoading.postValue(false)
                _upload.value = data
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _upload.value = false
                _onMessageError.postValue(error)
            }

        })
    }

    fun update(product : Product){
        _isViewLoading.postValue(true)
        repository.geneticOperation(OPERATION_UPDATE, product, object:UploadCallBack<Boolean>{
            override fun onSuccess(data: Boolean) {
                _isViewLoading.postValue(true)
                _update.value = data
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _update.value = false
                _onMessageError.postValue(error)
            }
        })

    }

    fun getProducts(){
        _isViewLoading.postValue(true)
        repository.retrieveProducts(object:OperationCallBack<Product>{
            override fun onSuccess(data: ArrayList<Product>) {
                _isViewLoading.postValue(false)
                    if(data.isEmpty()){
                        _isEmptyList.postValue(true)
                    }else{
                        _products.value = data
                    }

            }

            override fun onError(error: String?) {
                println(error)
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }

        })
    }









}