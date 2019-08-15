package com.ddphin.security.demo.system.id;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class IDWorkerAware {
    private SnowFlakeIDWorker idWorker;

    @Autowired
    public void setIdWorker(SnowFlakeIDWorkerFactory snowFlakeIDWorkerFactory) {
        this.idWorker = snowFlakeIDWorkerFactory.newWorker();
    }

    protected long nextId() {
        return this.idWorker.nextId();
    }


    protected String nextIdWtihTimestampPrefix(String pattern, int digits, int radix) {
        return new SimpleDateFormat(pattern).format(new Date(System.currentTimeMillis()))
                +
                StringUtils.leftPad(Long.toString(this.nextId(), radix), digits, "0");
    }

    protected String nextIdWtihCustomizedPrefix(String prefix, int digits, int radix) {
        return prefix
                +
                StringUtils.leftPad(Long.toString(this.nextId(), radix), digits, "0");
    }

    protected String nextIdWtihCustomizedPrefix(long prefix, int prefixRadix, int prefixDigits, int digits, int radix) {
        return StringUtils.leftPad(Long.toString(prefix, prefixRadix), prefixDigits, "0")
                +
                StringUtils.leftPad(Long.toString(this.nextId(), radix), digits, "0");
    }
}
