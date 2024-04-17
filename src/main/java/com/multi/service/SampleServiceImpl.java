package com.multi.service;

import org.springframework.stereotype.Service;

import com.multi.mapper.SampleMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

	private final SampleMapper sampleMapper;
	
	@Override
	public int getTableCount() {
		return sampleMapper.getTableCount();
	}

}
