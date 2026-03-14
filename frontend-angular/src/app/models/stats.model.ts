export interface ManufacturerStats {
  manufacturerName: string;
  cpuCount: number;
  avgCores: number;
  avgFrequency: number;
  avgBenchmarkScore: number;
}

export interface BenchmarkStats {
  cpuModel: string;
  manufacturerName: string;
  benchmarkCount: number;
  avgSingleCore: number;
  avgMultiCore: number;
  avgPassmark: number;
  avgCinebench: number;
}

export interface TechnologyUsage {
  technologyId: number;
  technologyName: string;
  releaseYear: number;
  cpuCount: number;
  avgCpuCores: number;
  avgCpuFrequency: number;
}
