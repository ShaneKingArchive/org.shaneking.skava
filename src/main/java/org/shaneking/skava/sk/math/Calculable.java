package org.shaneking.skava.sk.math;

public interface Calculable<M, N, E> {
  E calc(M m, N n);
}
