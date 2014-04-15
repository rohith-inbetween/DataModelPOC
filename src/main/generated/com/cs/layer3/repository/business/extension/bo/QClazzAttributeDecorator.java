package com.cs.layer3.repository.business.extension.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QClazzAttributeDecorator is a Querydsl query type for ClazzAttributeDecorator
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QClazzAttributeDecorator extends EntityPathBase<ClazzAttributeDecorator> {

    private static final long serialVersionUID = -105471734L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClazzAttributeDecorator clazzAttributeDecorator = new QClazzAttributeDecorator("clazzAttributeDecorator");

    public final com.cs.layer3.repository.business.defalt.bo.QBusinessObject _super = new com.cs.layer3.repository.business.defalt.bo.QBusinessObject(this);

    public final com.cs.layer3.repository.business.defalt.bo.QAttribute attribute;

    public final com.cs.layer3.repository.business.defalt.bo.QClazz clazz;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public QClazzAttributeDecorator(String variable) {
        this(ClazzAttributeDecorator.class, forVariable(variable), INITS);
    }

    public QClazzAttributeDecorator(Path<? extends ClazzAttributeDecorator> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QClazzAttributeDecorator(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QClazzAttributeDecorator(PathMetadata<?> metadata, PathInits inits) {
        this(ClazzAttributeDecorator.class, metadata, inits);
    }

    public QClazzAttributeDecorator(Class<? extends ClazzAttributeDecorator> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attribute = inits.isInitialized("attribute") ? new com.cs.layer3.repository.business.defalt.bo.QAttribute(forProperty("attribute")) : null;
        this.clazz = inits.isInitialized("clazz") ? new com.cs.layer3.repository.business.defalt.bo.QClazz(forProperty("clazz")) : null;
    }

}

