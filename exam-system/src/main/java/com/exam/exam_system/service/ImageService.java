package com.exam.exam_system.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageService {

	private Cloudinary cloudinary;

	@Autowired
	public ImageService(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	public String uploadImage(MultipartFile file) throws IOException {
		Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
		return result.get("secure_url").toString();
	}
}
