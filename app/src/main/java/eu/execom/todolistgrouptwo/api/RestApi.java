package eu.execom.todolistgrouptwo.api;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Delete;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Header;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Put;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import eu.execom.todolistgrouptwo.api.interceptor.AuthenticationInterceptor;
import eu.execom.todolistgrouptwo.constant.ApiConstants;
import eu.execom.todolistgrouptwo.model.Task;
import eu.execom.todolistgrouptwo.model.dto.RegisterDTO;
import eu.execom.todolistgrouptwo.model.dto.TokenContainerDTO;


@Rest(rootUrl = ApiConstants.ROOT_URL, converters = {GsonHttpMessageConverter.class,
        FormHttpMessageConverter.class},
        interceptors = AuthenticationInterceptor.class)
public interface RestApi {

    @Post(value = ApiConstants.REGISTER_PATH)
    ResponseEntity register(@Body RegisterDTO registerDTO);


    @Header(name = "Content-Type", value = "application/x-www-form-urlencoded")
    @Post(value = ApiConstants.LOGIN_PATH)
    TokenContainerDTO login(@Body LinkedMultiValueMap<String, String> accountInfo);


    @Get(value = ApiConstants.TASK_PATH)
    List<Task> getAllTasks();


    @Post(value = ApiConstants.TASK_PATH)
    Task createTask(@Body Task task);


    @Put(value = ApiConstants.TASK_PATH + "/{id}")
    Task updateTask(@Body Task task, @Path Long id);


    @Delete(value = ApiConstants.DELETE_PATH)
    void delete(@Path Long id);
}
