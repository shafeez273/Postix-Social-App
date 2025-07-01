package oth.ics.wtp.postixbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.postixbackend.ClientErrors;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.entities.Like;
import oth.ics.wtp.postixbackend.entities.Post;
import oth.ics.wtp.postixbackend.repositories.LikeRepository;
import oth.ics.wtp.postixbackend.repositories.PostRepository;

@Service public class LikeService {
    private final LikeRepository likeRepo;
    private final PostRepository postRepo;

    @Autowired public LikeService(LikeRepository likeRepo, PostRepository postRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
    }

    public int numberOfLikes(long postId) {
        if (!postRepo.existsById(postId)) {
            throw ClientErrors.postNotFound();
        }
        return likeRepo.countByPostId(postId);
    }

    public void likePost(long postId, AppUser user) {
        Post post = postRepo.findById(postId).orElseThrow(ClientErrors::postNotFound);

        if (post.getUser().getUsername().equals(user.getUsername())) {
            throw ClientErrors.cannotLikeOwnPost();
        }

        if (likeRepo.existsByPostIdAndUserUsername(postId, user.getUsername())) {
            throw ClientErrors.alreadyLiked(postId);
        }

        Like like = new Like(user, post);
        likeRepo.save(like);
    }

    public void unlikePost(String username, long postId) {
        Like like = likeRepo.findByPostIdAndUserUsername(postId, username)
                .orElseThrow(ClientErrors::likeNotFound);
        likeRepo.delete(like);
    }



    public boolean isLiked(long postId, String userName) {
        if (!postRepo.existsById(postId)) {
            throw ClientErrors.postNotFound();
        }
        return likeRepo.existsByPostIdAndUserUsername(postId, userName);
    }
}
