package com.example.repos;


import com.example.domain.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {

    List<Message> findByTag(String tag);

    @Modifying
    @Query("delete from Message m where m.id =:id")
    void deleteMessageById(@Param("id") Long id);

}
