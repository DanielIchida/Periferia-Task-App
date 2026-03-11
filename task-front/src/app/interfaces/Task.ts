export interface ResulTask {
  code:    number;
  message: string;
  error:   null;
  data:    Data | Task | boolean;
}

export interface Data {
  content:          Task[];
  pageable:         Pageable;
  totalPages:       number;
  totalElements:    number;
  last:             boolean;
  numberOfElements: number;
  first:            boolean;
  size:             number;
  number:           number;
  sort:             Sort;
  empty:            boolean;
}

export interface Task {
  id: number,
  title: string,
  description?: string,
  status?: string,
  createdAt?: string
}

export interface Pageable {
  pageNumber: number;
  pageSize:   number;
  sort:       Sort;
  offset:     number;
  paged:      boolean;
  unpaged:    boolean;
}

export interface Sort {
  sorted:   boolean;
  unsorted: boolean;
  empty:    boolean;
}
