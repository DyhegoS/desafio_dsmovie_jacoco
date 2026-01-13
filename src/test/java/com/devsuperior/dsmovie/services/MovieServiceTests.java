package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;
	
	@Mock
	private MovieRepository repository;
	
	private long existingMovieId, nonExistingMovieId;
	
	private MovieEntity movieEntity;
	private MovieDTO movieDTO;
	private PageImpl<MovieEntity> page;
	
	@BeforeEach
	void setUp() throws Exception{
		existingMovieId = 1L;
		nonExistingMovieId = 2L;
		movieEntity = MovieFactory.createMovieEntity();
		movieDTO = MovieFactory.createMovieDTO();
		page = new PageImpl<>(List.of(movieEntity));
		
		Mockito.when(repository.searchByTitle(any(), (Pageable)any())).thenReturn(page);
		
		Mockito.when(repository.findById(existingMovieId)).thenReturn(Optional.of(movieEntity));
		Mockito.when(repository.findById(nonExistingMovieId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.save(any())).thenReturn(movieEntity);
	}
	
	@Test
	public void findAllShouldReturnPagedMovieDTO() {
		Pageable pageable = PageRequest.of(0, 12);
		String title = movieEntity.getTitle();
		Page<MovieDTO> result = service.findAll(title, pageable);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.iterator().next().getTitle(), title);
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
		MovieDTO result = service.findById(existingMovieId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingMovieId);
		Assertions.assertEquals(result.getTitle(), movieEntity.getTitle());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingMovieId);
		});
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
		MovieDTO result = service.insert(movieDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), movieDTO.getId());
		Assertions.assertEquals(result.getTitle(), movieDTO.getTitle());
	}
	
//	@Test
//	public void updateShouldReturnMovieDTOWhenIdExists() {
//	}
//	
//	@Test
//	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
//	}
//	
//	@Test
//	public void deleteShouldDoNothingWhenIdExists() {
//	}
//	
//	@Test
//	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
//	}
//	
//	@Test
//	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
//	}
}
