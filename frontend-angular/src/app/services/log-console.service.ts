import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type LogLevel = 'log' | 'info' | 'warn' | 'error';

export interface LogEntry {
  id: string;
  level: LogLevel;
  message: string;
  timestamp: string;
}

@Injectable({ providedIn: 'root' })
export class LogConsoleService {
  private static readonly MAX_LOGS = 300;

  private readonly logsSubject = new BehaviorSubject<LogEntry[]>([]);
  readonly logs$ = this.logsSubject.asObservable();

  private readonly originalConsole: Partial<Record<LogLevel, (...args: unknown[]) => void>> = {};
  private patched = false;

  constructor() {
    this.patchConsole();
  }

  clear(): void {
    this.logsSubject.next([]);
  }

  addSystemLog(level: LogLevel, message: string): void {
    this.append(level, [message]);
  }

  private patchConsole(): void {
    if (this.patched) {
      return;
    }

    (['log', 'info', 'warn', 'error'] as LogLevel[]).forEach((level) => {
      const original = console[level].bind(console);
      this.originalConsole[level] = original;

      console[level] = (...args: unknown[]) => {
        this.append(level, args);
        original(...args);
      };
    });

    this.patched = true;
    this.addSystemLog('info', '[LogConsole] Console capture is active');
  }

  private append(level: LogLevel, args: unknown[]): void {
    const nextEntry: LogEntry = {
      id: this.generateId(),
      level,
      message: args.map((arg) => this.stringifyArg(arg)).join(' '),
      timestamp: new Date().toISOString()
    };

    const next = [nextEntry, ...this.logsSubject.value].slice(0, LogConsoleService.MAX_LOGS);
    this.logsSubject.next(next);
  }

  private stringifyArg(arg: unknown): string {
    if (typeof arg === 'string') {
      return arg;
    }

    if (arg instanceof Error) {
      return `${arg.name}: ${arg.message}`;
    }

    try {
      return JSON.stringify(arg);
    } catch {
      return String(arg);
    }
  }

  private generateId(): string {
    if (typeof crypto !== 'undefined' && typeof crypto.randomUUID === 'function') {
      return crypto.randomUUID();
    }
    return `${Date.now()}-${Math.random().toString(36).slice(2)}`;
  }
}
