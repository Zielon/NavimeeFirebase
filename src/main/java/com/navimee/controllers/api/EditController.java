package com.navimee.controllers.api;

import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.controllers.dto.FieldUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "api/edit")
public class EditController {

    @Autowired
    UsersRepository usersRepository;

    @RequestMapping(value = "update/usersField", method = RequestMethod.POST)
    public Future<Void> editUsersFileds(@RequestBody FieldUpdateDto dto) throws Exception {

        String type = dto.getClassType().toUpperCase();
        Object value;

        switch (type){
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

        if(value == null)
            throw new Exception("The value is null!");

        return usersRepository.updateUsersField(dto.getFiled(), value);
    }

    @RequestMapping(value = "delete/usersField/{field}", method = RequestMethod.POST)
    public Future<Void> deleteUsersFileds(@PathVariable String field) throws NoSuchFieldException {
        return usersRepository.deleteUsersField(field);
    }
}
