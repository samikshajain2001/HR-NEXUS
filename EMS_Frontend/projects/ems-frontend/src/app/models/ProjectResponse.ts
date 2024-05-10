import { Project } from "./Project";

export interface ProjectResponse{
    content: Project[];
    pageable:{
        pageNumber: number;
        pageSize: number;
        paged: boolean;
        unpaged: boolean;
    };
    size: number ;
    totalElement: boolean;
    totalPages: number;
}

    
