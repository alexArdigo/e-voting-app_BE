package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.HelpComment;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public HelpComment comment(String comentario) {
        HelpComment helpComment = new HelpComment();
        helpComment.setComment(comentario);
        // Assuming there's a repository to save the comment, e.g., helpCommentRepository.save(helpComment);
        // For now, we will just return the created comment object.
        return helpComment;
    }
}
