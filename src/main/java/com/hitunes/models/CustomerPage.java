package com.hitunes.models;

import java.util.List;

public record CustomerPage(List<Customer> customers, int offset, int limit) {}
;
