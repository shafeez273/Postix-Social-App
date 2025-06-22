package oth.ics.wtp.postixbackend.dtos;

import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.entities.Post;

public record LikeDto(long id, AppUser user, Post post) { }
