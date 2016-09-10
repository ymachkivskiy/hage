package org.hage.platform.component.loadbalance.knapsack.balancing.pack;

import org.aspectj.lang.annotation.Pointcut;

class PackerPointcuts {

    @Pointcut(value = "execution(public * AllocationsPacker.repack(..))")
    static void repackPointcut() {
    }

}
