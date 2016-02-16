package com.nix;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
  private Long GUID;
  private String name;

  private Price price;
}
