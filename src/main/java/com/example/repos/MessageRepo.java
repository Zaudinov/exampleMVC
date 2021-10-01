package com.example.repos;

import com.example.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {

    public List<Message> findByTag(String tag);
}
