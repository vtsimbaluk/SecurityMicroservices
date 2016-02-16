package com.nix;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Price {
  private Long GUID;
  private Double price;
  private Double discount;
}
