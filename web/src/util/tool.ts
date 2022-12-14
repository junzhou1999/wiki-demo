export class Tool {
    /**
     * 空校验 null或""都返回true
     */
    public static isEmpty(obj: any) {
        if ((typeof obj === 'string')) {
            return !obj || obj.replace(/\s+/g, "") === ""
        } else {
            return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
        }
    }

    /**
     * 非空校验
     */
    public static isNotEmpty(obj: any) {
        return !this.isEmpty(obj);
    }

    /**
     * 对象复制
     * @param obj
     */
    public static copy(obj: object) {
        if (Tool.isNotEmpty(obj)) {
            const cloneDeep = require('lodash.clonedeep');
            return cloneDeep(obj);
        }
    }

    /**
     * 递归将数组转换成树形结构
     * @param array
     * @param parentId
     */
    public static array2Tree(array: any, parentId: number) {
        if (Tool.isEmpty(array)) {
            return [];
        }
        const result = [];
        for (let i = 0; i < array.length; i++) {
            const c = array[i];
            // 递归条件
            c.parent = Object(c.parent);
            if (Number(c.parent) === Number(parentId)) {
                result.push(c);  // push了children对象和父属性

                //递归查看当前节点对应的子节点
                const children = Tool.array2Tree(array, c.id);
                if (Tool.isNotEmpty(children)) {
                    c.children = children;
                }
            }
        }
        return result;
    }

    /**
     * 随机生成[len]长度的[radix]进制数
     * @param len
     * @param radix 默认62
     * @returns {string}
     */
    public static uuid(len: number, radix = 62) {
        // 字符串拆分成字符数组
        const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        const uuid = [];
        radix = radix || chars.length;

        for (let i = 0; i < len; i++) {
            // 按位与，0的小数位依然是0所以得出来得数是随机数的整数位
            uuid[i] = chars[0 | Math.random() * radix];
        }

        return uuid.join('');  // 字符数组拼接成字符串
    }
}