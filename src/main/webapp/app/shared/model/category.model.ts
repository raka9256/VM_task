import { ICategory } from 'app/shared/model/category.model';

export interface ICategory {
    id?: number;
    name?: string;
    parentCategoryId?: number;
    subCategories?: ICategory[];
}

export class Category implements ICategory {
    constructor(public id?: number, public name?: string, public parentCategoryId?: number, public subCategories?: ICategory[]) {}
}
