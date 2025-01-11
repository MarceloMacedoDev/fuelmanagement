import { Abastecimento } from "./abastecimento";
import { Page } from "./page";

export interface AbastecimentoPageResponse {
    content: Abastecimento[];
    pageable?: Page;
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
    sort: any;
    numberOfElements: number;
    first: boolean;
    last: boolean;
    empty: boolean;
}