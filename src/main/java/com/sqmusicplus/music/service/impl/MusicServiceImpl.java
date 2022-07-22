package com.sqmusicplus.music.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqmusicplus.music.entity.Music;
import com.sqmusicplus.music.mapper.MusicMapper;
import com.sqmusicplus.music.service.IMusicService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌曲列表 服务实现类
 * </p>
 *
 * @author SQ
 * @since 2022-05-16
 */
@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements IMusicService {

}
