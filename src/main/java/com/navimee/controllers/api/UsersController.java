package com.navimee.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.controllers.dto.FieldUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "api/edit")
public class UsersController {

    @Autowired
    UsersRepository usersRepository;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "update/usersField", method = RequestMethod.POST)
    public Future<Void> editUsersFileds(@RequestBody FieldUpdateDto dto) throws Exception {

        String type = dto.getClassType().toUpperCase();
        Object value;

        switch (type) {
            case "BOOLEAN":
                value = Boolean.parseBoolean(dto.getValue());
                break;
            case "INTEGER":
                value = Integer.parseInt(dto.getValue());
                break;
            case "STRING":
                value = dto.getValue();
                break;
            default:
                throw new Exception("The given type is not supported!");
        }

        if (value == null)
            throw new Exception("The value is null!");

        return usersRepository.updateUsersField(dto.getFiled(), value);
    }

    @RequestMapping(value = "delete/usersField/{field}", method = RequestMethod.POST)
    public Future<Void> deleteUsersFileds(@PathVariable String field) throws NoSuchFieldException {
        return usersRepository.deleteUsersField(field);
    }

    @RequestMapping(value = "delete/usersCollection/{collection}", method = RequestMethod.POST)
    public Future<Void> deleteUsersCollection(@PathVariable String collection) throws NoSuchFieldException {
        return usersRepository.deleteUsersCollection(collection);
    }

    @RequestMapping(value = "get/users", method = RequestMethod.GET, produces = "application/json")
    public Future<String> events() {
        return usersRepository.getAllUsers().thenApplyAsync(users -> {
            try {
                return mapper.writeValueAsString(users);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return "Error";
        });
    }
}
