package org.shaneking.skava.math;

public interface Calculable<M, N, E> {
  E calc(M m, N n);
}
