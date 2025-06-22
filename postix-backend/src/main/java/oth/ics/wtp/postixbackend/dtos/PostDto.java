package oth.ics.wtp.postixbackend.dtos;

import java.time.Instant;

public record PostDto(long id, String message, Instant timestamp, int numberOfLikes) { }
