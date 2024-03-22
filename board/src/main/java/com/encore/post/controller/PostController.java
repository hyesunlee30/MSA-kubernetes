package com.encore.post.controller;

import com.encore.common.support.DefaultResponse;
import com.encore.common.support.ResponseCode;
import com.encore.common.support.SomException;
import com.encore.post.domain.Post;
import com.encore.post.dto.PostDetailResDto;
import com.encore.post.dto.PostReqDto;
import com.encore.post.dto.PostResDto;
import com.encore.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/board/post")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create") // Post create
    public DefaultResponse<Long> postCreate(@Valid PostReqDto postReqDto, HttpServletRequest httpServletRequest, BindingResult bindingResult){
        System.out.println("filteredContents1 = " + httpServletRequest.getAttribute("filteredContents"));
        String filteredContents = String.valueOf(httpServletRequest.getAttribute("filteredContents")); // 욕설 필터링
        System.out.println("filteredContents = " + filteredContents);
        if (bindingResult.hasErrors()) {
            throw new SomException(ResponseCode.valueOf(bindingResult.getAllErrors().get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
        }
        if (filteredContents != null) {
            postReqDto.setContents(filteredContents);
        }

        Post post = postService.create(postReqDto);
        return new DefaultResponse<>(post.getId());
    }

    @GetMapping("/list")
    public DefaultResponse.PagedResponse<PostResDto> postList(@RequestParam String title, Pageable pageable) {
        return new DefaultResponse.PagedResponse<>(postService.findAll(title, pageable));
    }

    @GetMapping("/{id}/detail")
    public DefaultResponse<PostDetailResDto> postDetail(@PathVariable Long id){
        PostDetailResDto postDetailResDto = postService.findPostDetail(id);
        return new DefaultResponse<>(postDetailResDto);
    }

    @PostMapping("/{id}/update")
    public DefaultResponse<Post> postUpdate(@PathVariable Long id, PostReqDto postReqDto, HttpServletRequest httpServletRequest) {
        String filteredContents = (String) httpServletRequest.getAttribute("filteredContents"); // 욕설 필터링
        if (filteredContents != null) {
            postReqDto.setContents(filteredContents);
        }
        Post post = postService.update(id, postReqDto);
        return new DefaultResponse<>(post);
    }

    @DeleteMapping("/{id}/delete")
    public DefaultResponse<Long> postDelete(@PathVariable Long id) {
        Post post = postService.delete(id);

        return new DefaultResponse<Long>(post.getId());
    }
}
