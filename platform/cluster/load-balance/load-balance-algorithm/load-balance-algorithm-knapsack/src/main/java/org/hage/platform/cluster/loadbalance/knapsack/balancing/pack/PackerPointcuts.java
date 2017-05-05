package org.hage.platform.cluster.loadbalance.knapsack.balancing.pack;

import org.aspectj.lang.annotation.Pointcut;

class PackerPointcuts {

    @Pointcut(value = "execution(public * org.hage.platform.cluster.loadbalance.knapsack.balancing.pack.AllocationsPacker.repack(..))")
    static void repackPointcut() {
    }

}
