package oth.ics.wtp.postixbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.postixbackend.ClientErrors;
import oth.ics.wtp.postixbackend.dtos.LikeDto;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.entities.Like;
import oth.ics.wtp.postixbackend.entities.Post;
import oth.ics.wtp.postixbackend.repositories.LikeRepository;
import oth.ics.wtp.postixbackend.repositories.PostRepository;

import java.util.Objects;

@Service public class LikeService {
    private final LikeRepository likeRepo;
    private final PostRepository postRepo;

    @Autowired public LikeService(LikeRepository likeRepo, PostRepository postRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
    }

    public int numberOfLikes(long postId) {
        if (postRepo.existsById(postId)) {
            throw ClientErrors.postNotFound();
        }
        return likeRepo.findByPostId(postId).stream().map(this::toDto).toList().size();
    }

    public void setLike(long postId, AppUser user) {
        Post post = postRepo.findById(postId).orElseThrow(ClientErrors::postNotFound);
        if (!likeRepo.existsByPostIdAndUserUsername(postId, user.getUsername()) || Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            Like like = new Like(user, post);
            likeRepo.save(like);
        }
    }

    private LikeDto toDto(Like like) {
        return new LikeDto(like.getId(), like.getUser(), like.getPost());
    }
}
