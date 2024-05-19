package blogging.service;

import blogging.dto.PageResponse;
import blogging.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PageResponse<PostDto> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto getPostById(Long id);
    PostDto updatePost(PostDto postDto, Long id);
    void deletePost(Long id);

    List<PostDto> getPostsByCategory(Long categoryId);
}
