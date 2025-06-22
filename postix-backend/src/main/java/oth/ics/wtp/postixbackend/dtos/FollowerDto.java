package oth.ics.wtp.postixbackend.dtos;

import oth.ics.wtp.postixbackend.entities.AppUser;

public record FollowerDto(long id, AppUser followerName, AppUser followingName) { }
