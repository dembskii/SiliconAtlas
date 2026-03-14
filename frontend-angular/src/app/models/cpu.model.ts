export interface Cpu {
  id: string;
  model: string;
  cores: number;
  threads: number;
  frequencyGhz: number;
  manufacturerName: string;
  manufacturerId: string;
  technologyNames: string[];
  technologyIds: string[];
  specification?: CpuSpecification;
}

export interface CpuCreate {
  model: string;
  cores: number;
  threads: number;
  frequencyGhz: number;
  manufacturerId: string;
  technologyIds: string[];
  specification?: CpuSpecificationCreate;
}

export interface CpuDetails {
  id: string;
  model: string;
  cores: number;
  threads: number;
  frequencyGhz: number;
  manufacturerName: string;
  manufacturerId: string;
  technologyNames: string[];
  specification?: CpuSpecification;
  benchmarks?: CpuBenchmark[];
}

export interface CpuSpecification {
  id: string;
  cacheL1KB: number;
  cacheL2KB: number;
  cacheL3MB: number;
  tdpWatts: number;
  socketType: string;
}

export interface CpuSpecificationCreate {
  cacheL1KB: number;
  cacheL2KB: number;
  cacheL3MB: number;
  tdpWatts: number;
  socketType: string;
}

export interface CpuBenchmark {
  id: string;
  singleCoreScore: number;
  multiCoreScore: number;
  passmarkScore: number;
  cinebenchR23: number;
  testDate: string;
  cpuId: string;
  cpuModel: string;
}

export interface CpuSearchCriteria {
  model?: string;
  manufacturer?: string;
  minCores?: number;
  maxCores?: number;
  minThreads?: number;
  maxThreads?: number;
  minFrequency?: number;
  maxFrequency?: number;
  technology?: string;
}

export interface PagedResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
}
