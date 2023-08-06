package com.example.goalmates.controller;

import com.example.goalmates.dto.PostUserIdDTO;
import com.example.goalmates.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("")
    public void addComment(@RequestBody PostUserIdDTO postUserIdDTO) {
        commentService.addComment(postUserIdDTO);
    }
}
