import { Employee } from "./Employee";

export interface EmployeeResponse{
    content: Employee[];
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