import { Department } from "./Department";

export interface Project{
    projectId: number;
    projectTitle: string;
    projectStatus: string;
    department: Department;
    start_date: any;
    end_date: any;
    projectDesc: string;
}