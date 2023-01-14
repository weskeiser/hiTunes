package com.hitunes.models;

import java.util.List;

public record TopGenre(int customerId, List<String> genreList, int occurences) {}
;
