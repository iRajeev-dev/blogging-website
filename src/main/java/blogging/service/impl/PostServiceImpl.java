package blogging.service.impl;
import blogging.dto.PageResponse;
import blogging.dto.PostDto;
import blogging.entity.Category;
import blogging.entity.PostEntity;
import blogging.exception.ResourceNotFoundException;
import blogging.repository.CategoryRepository;
import blogging.repository.PostRepository;
import blogging.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostDto createPost(PostDto postDto) {
        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category", "id", postDto.getCategoryId().toString()));
        PostEntity postEntity = modelMapper.map(postDto, PostEntity.class);
        postEntity.setCategory(category);
        PostEntity createdPost = postRepository.save(postEntity);
        return modelMapper.map(createdPost, PostDto.class);
    }

    @Override
    public PageResponse<PostDto> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        //creating instance of Pageable
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<PostEntity> postEntityPage = postRepository.findAll(pageable);
        //Get Content of Page object
        List<PostEntity> postEntityList = postEntityPage.getContent();

        List<PostDto> content = postEntityList.stream()
                .map(postEntity -> modelMapper.map(postEntity, PostDto.class))
                .toList();
        //Creating object of PageResponse
        PageResponse<PostDto> pageResponse = new PageResponse<>();
        pageResponse.setContent(content);
        pageResponse.setPageNo(postEntityPage.getNumber());
        pageResponse.setPageSize(postEntityPage.getSize());
        pageResponse.setTotalPages(postEntityPage.getTotalPages());
        pageResponse.setTotalElements(postEntityPage.getTotalElements());
        pageResponse.setLastPage(postEntityPage.isLast());

        return pageResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post", "id", id.toString()));
        return modelMapper.map(postEntity, PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post", "id", id.toString()));

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category", "id", postDto.getCategoryId().toString()));
        postDto.setId(postEntity.getId());
        PostEntity updatedPost = modelMapper.map(postDto, PostEntity.class);
        postEntity.setCategory(category);
        PostEntity savedPost = postRepository.save(updatedPost);
        return modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public void deletePost(Long id) {
        //get post by id from database
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post", "id", id.toString()));
        //deleting the post with given id
        postRepository.delete(postEntity);
    }

    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "id", categoryId.toString()));
        List<PostEntity> posts = postRepository.findByCategoryId(categoryId);
        return posts.stream().map(post -> modelMapper.map(post, PostDto.class)).toList();
    }
}
