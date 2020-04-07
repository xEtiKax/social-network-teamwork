package com.example.demo.services;

import com.example.demo.models.Comments;
import com.example.demo.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comments getById(int commentId) {
        return commentRepository.getById(commentId);
    }

    @Override
    public List<Comments> getCommentsByPostId(int postId) {
        return commentRepository.getCommentsByPostId(postId);
    }

    @Override
    public Comments createComment(Comments comments) {
        commentRepository.save(comments);
        return comments;
    }

    @Override
    public Comments updateComment(Comments comments) {
        commentRepository.save(comments);
        return comments;
    }

    @Override
    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }
}
