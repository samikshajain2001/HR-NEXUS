import { Department } from "./Department";

export interface DepartmentResponse {
    content: Department[];
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