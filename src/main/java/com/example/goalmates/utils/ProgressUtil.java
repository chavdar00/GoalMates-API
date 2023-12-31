package com.example.goalmates.utils;

import com.example.goalmates.exception.BadRequestException;
import com.example.goalmates.models.Post;
import com.example.goalmates.models.PostUpdates;
import com.example.goalmates.repository.PostRepository;
import com.example.goalmates.repository.PostUpdatesRepository;
import com.example.goalmates.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

@Component
public class ProgressUtil {
    @Autowired
    private PostUpdatesRepository postUpdatesRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ProgressRepository progressRepository;
    public void calcUpdateProgress(Long postUpdateId){
        Optional<PostUpdates> postUpdates = postUpdatesRepository.findById(postUpdateId);
        if (postUpdates.isEmpty()){
            throw new BadRequestException("Update not found");
        }
        Optional<Post> post = postRepository.findById(postUpdates.get().getPost().getId());
        if (post.isEmpty()) {
            throw new BadRequestException("Post not found");
        }
        List<BigDecimal> progress = progressRepository.findAllProgressByPostUpdatesId(postUpdateId);
        double sum = 0.0;
        for (BigDecimal rating : progress) {
            sum += rating.doubleValue();
        }
        double averageRating = sum / progress.size();
        double normalizedRating = ((averageRating)/5) *100;
        postUpdates.get().setTotalProgress(BigDecimal.valueOf(normalizedRating));

        postUpdatesRepository.save(postUpdates.get());
        List<PostUpdates> up = postUpdatesRepository.findAllByPostId(post.get().getId());
        List<BigDecimal> dec = new ArrayList<>();
        up.forEach(updates->{
            dec.add(updates.getTotalProgress());
        });
        double total =0.0;
        for (BigDecimal rating : dec) {
            total += rating.doubleValue();
        }
        post.get().setProgress(BigDecimal.valueOf(total/up.size()));
        postRepository.save(post.get());
    }
}
