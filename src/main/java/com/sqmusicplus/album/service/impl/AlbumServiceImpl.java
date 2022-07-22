package com.sqmusicplus.album.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqmusicplus.album.entity.Album;
import com.sqmusicplus.album.mapper.AlbumMapper;
import com.sqmusicplus.album.service.IAlbumService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专辑	 服务实现类
 * </p>
 *
 * @author SQ
 * @since 2022-05-16
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements IAlbumService {

}
