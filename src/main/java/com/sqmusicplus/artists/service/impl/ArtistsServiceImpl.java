package com.sqmusicplus.artists.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqmusicplus.artists.entity.Artists;
import com.sqmusicplus.artists.mapper.ArtistsMapper;
import com.sqmusicplus.artists.service.IArtistsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 艺术家	 服务实现类
 * </p>
 *
 * @author SQ
 * @since 2022-05-16
 */
@Service
public class ArtistsServiceImpl extends ServiceImpl<ArtistsMapper, Artists> implements IArtistsService {

}
