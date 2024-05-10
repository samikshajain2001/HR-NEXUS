import { Paygrade } from "./Paygrade";

export interface PaygradeResponse {
    content: Paygrade[];
    pageable: {
      pageNumber: number;
      pageSize: number;
      paged: boolean;
      unpaged: boolean;
    };
    size: number;
    totalElements: number;
    totalPages: number;
  }