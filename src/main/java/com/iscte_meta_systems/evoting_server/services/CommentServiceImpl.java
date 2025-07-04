package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Answer;
import com.iscte_meta_systems.evoting_server.entities.HelpComment;
import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.VoterHash;
import com.iscte_meta_systems.evoting_server.enums.Role;
import com.iscte_meta_systems.evoting_server.repositories.AnswerRepository;
import com.iscte_meta_systems.evoting_server.repositories.HelpCommentRepository;
import com.iscte_meta_systems.evoting_server.repositories.VoterHashRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private HelpCommentRepository helpCommentRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VoterService voterService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VoterHashRepository voterHashRepository;

    @Override
    public HelpComment comment(String comentario) {
        HelpComment helpComment = new HelpComment();
        helpComment.setComment(comentario);
        helpCommentRepository.save(helpComment);
        return helpComment;
    }

    @Override
    public Answer answerComment(String answer, Long id) {
        User user = userService.getCurrentUser();
        if (Role.ADMIN!= user.getRole()) {
            throw new RuntimeException("Only admins can answer comments.");
        }

        HelpComment helpComment = getCommentById(id);

        Answer answerEntity = new Answer();
        answerEntity.setAnswer(answer);
        answerEntity.setComment(helpComment);
        answerEntity.setAdminId(user.getId());

        answerRepository.save(answerEntity);

        helpComment.setAnswer(answerEntity);
        helpCommentRepository.save(helpComment);

        return answerEntity;
    }

    @Override
    public HelpComment getCommentById(Long id) {
        return helpCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }

    @Override
    public boolean likeComment(Long id) {
        HelpComment comment = getCommentById(id);
        String nif = voterService.getInfo().getNif().toString();

        boolean alreadyLiked = comment.getLikedBy().stream()
                .anyMatch(vh -> passwordEncoder.matches(nif, vh.getVoterHash()));

        if (alreadyLiked) {
            return false;
        }

        String hash = passwordEncoder.encode(nif);

        VoterHash newLike = new VoterHash();
        newLike.setVoterHash(hash);
        comment.addLike(newLike);

        voterHashRepository.save(newLike);

        helpCommentRepository.save(comment);
        return true;
    }


    @Override
    public List<HelpComment> getAllComments() {
        return helpCommentRepository.findAll();
    }

    @Override
    public boolean hasUserLiked(Long commentId) {
        HelpComment comment = getCommentById(commentId);
        String nif = voterService.getInfo().getNif().toString();

        return comment.getLikedBy().stream()
                .anyMatch(vh -> passwordEncoder.matches(nif, vh.getVoterHash()));
    }

    @PostConstruct
    public void init() {
        if (helpCommentRepository.count() != 0) {
            return;
        }
        HelpComment comment1 = new HelpComment();
        comment1.setComment("Posso votar com 16 anos?");
        helpCommentRepository.save(comment1);

        HelpComment comment2 = new HelpComment();
        comment2.setComment("Como posso alterar o meu voto?");
        helpCommentRepository.save(comment2);

        Answer answer = new Answer();
        answer.setAnswer("O voto, assim que registado, já não pode ser alterado.");
        answer.setComment(comment2);
        answer.setAdminId(1L);

        answerRepository.save(answer);

        comment2.setAnswer(answer);
        helpCommentRepository.save(comment2);

    }
}
