package com.nix;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Price {
  private Long GUID;
  private Double price;
  private Double discount;
}
